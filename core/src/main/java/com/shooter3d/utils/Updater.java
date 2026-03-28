package com.shooter3d.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpResponseListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Updater {
    // Замени на свой GitHub username и repo
    private static final String GITHUB_USER = "gglgogaz1989-sketch";
    private static final String GITHUB_REPO = "Shooter-game";
    
    private static final String GITHUB_API = "https://api.github.com/repos/" + GITHUB_USER + "/" + GITHUB_REPO + "/releases/latest";
    private static final String CURRENT_VERSION = "1.0.0"; // Обновляй вручную при каждом релизе
    
    public interface UpdateListener {
        void onUpdateAvailable(String version, String downloadUrl);
        void onNoUpdate();
        void onError(String error);
        void onDownloadProgress(float progress);
        void onDownloadComplete(String filePath);
    }
    
    private static boolean isDownloading = false;
    private static String downloadingUrl = "";
    
    /**
     * Проверяет наличие новой версии на GitHub
     */
    public static void checkForUpdates(UpdateListener listener) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(GITHUB_API)
                .timeout(5000)
                .build();
        
        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    String response = httpResponse.getResultAsString();
                    JsonValue json = new JsonReader().parse(response);
                    
                    String latestVersion = json.getString("tag_name", "").replace("v", "");
                    String downloadUrl = null;
                    
                    // Ищем APK в assets релиза
                    JsonValue assets = json.get("assets");
                    if (assets != null && assets.size > 0) {
                        for (int i = 0; i < assets.size; i++) {
                            String name = assets.get(i).getString("name", "");
                            if (name.endsWith(".apk")) {
                                downloadUrl = assets.get(i).getString("browser_download_url", null);
                                break;
                            }
                        }
                    }
                    
                    // Если APK не найден, пробуем взять из тела релиза
                    if (downloadUrl == null) {
                        downloadUrl = json.getString("html_url", null);
                    }
                    
                    if (isNewerVersion(latestVersion, CURRENT_VERSION) && downloadUrl != null) {
                        Gdx.app.postRunnable(() -> listener.onUpdateAvailable(latestVersion, downloadUrl));
                    } else {
                        Gdx.app.postRunnable(() -> listener.onNoUpdate());
                    }
                } catch (Exception e) {
                    Gdx.app.postRunnable(() -> listener.onError("Parse error: " + e.getMessage()));
                }
            }
            
            @Override
            public void failed(Throwable t) {
                Gdx.app.postRunnable(() -> listener.onError("Network error: " + t.getMessage()));
            }
            
            @Override
            public void cancelled() {
                Gdx.app.postRunnable(() -> listener.onError("Update check cancelled"));
            }
        });
    }
    
    /**
     * Скачивает APK и устанавливает (Android) или сохраняет (Desktop)
     */
    public static void downloadAndInstall(String downloadUrl, UpdateListener listener) {
        if (isDownloading) {
            listener.onError("Download already in progress");
            return;
        }
        
        isDownloading = true;
        downloadingUrl = downloadUrl;
        
        String fileName = "shooter3d_update.apk";
        final FileHandle targetFile;
        
        if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android) {
            // Android: сохраняем во внешнее хранилище
            targetFile = Gdx.files.external(fileName);
        } else {
            // Desktop: сохраняем в папку игры
            targetFile = Gdx.files.local(fileName);
        }
        
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder.newRequest()
                .method(Net.HttpMethods.GET)
                .url(downloadUrl)
                .timeout(30000)
                .build();
        
        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
            private long lastProgress = 0;
            
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                byte[] data = httpResponse.getResult();
                targetFile.writeBytes(data, false);
                isDownloading = false;
                
                Gdx.app.postRunnable(() -> {
                    listener.onDownloadComplete(targetFile.path());
                    
                    // На Android запускаем установку
                    if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android) {
                        installApk(targetFile.path());
                    }
                });
            }
            
            @Override
            public void failed(Throwable t) {
                isDownloading = false;
                Gdx.app.postRunnable(() -> listener.onError("Download failed: " + t.getMessage()));
            }
            
            @Override
            public void cancelled() {
                isDownloading = false;
                Gdx.app.postRunnable(() -> listener.onError("Download cancelled"));
            }
        });
    }
    
    /**
     * Запускает установку APK на Android
     */
    private static native void installApk(String filePath);
    
    private static boolean isNewerVersion(String latest, String current) {
        try {
            String[] latestParts = latest.split("\\.");
            String[] currentParts = current.split("\\.");
            int maxLen = Math.max(latestParts.length, currentParts.length);
            
            for (int i = 0; i < maxLen; i++) {
                int latestNum = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;
                int currentNum = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
                if (latestNum > currentNum) return true;
                if (latestNum < currentNum) return false;
            }
            return false;
        } catch (Exception e) {
            return !latest.equals(current);
        }
    }
            }
