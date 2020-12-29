package com.example.demo.services;

import java.io.IOException;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import org.springframework.stereotype.Service;

@Service
public class FirebaseAppService {

    public static FirebaseAuth getFirebaseApp() throws IOException {
        InputStream serviceAccount = FirebaseAppService.class.getResourceAsStream("/wajeb-project-firebase-adminsdk-sgo80-e335aab765.json");    
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
        FirebaseApp defaultApp = FirebaseApp.initializeApp(options);
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance(defaultApp);
        return defaultAuth;
    }
    
}
