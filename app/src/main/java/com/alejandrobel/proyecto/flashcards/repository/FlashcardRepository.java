package com.alejandrobel.proyecto.flashcards.repository;

import com.alejandrobel.proyecto.flashcards.models.Flashcard;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.Date;
import java.util.List;

public class FlashcardRepository {
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "flashcards";

    public FlashcardRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void createFlashcard(Flashcard flashcard, OperationCallback callback) {
        flashcard.setCreatedAt(new Date());
        flashcard.setUpdatedAt(new Date());

        db.collection(COLLECTION_NAME)
                .add(flashcard)
                .addOnSuccessListener(documentReference -> {
                    flashcard.setId(documentReference.getId());
                    callback.onSuccess();
                })
                .addOnFailureListener(callback::onError);
    }

    // Implementa aquí los demás métodos CRUD (getAll, update, delete, etc.)

    public interface OperationCallback {
        void onSuccess();
        void onError(Exception e);
    }
}