// firebase.js
import dotenv from "dotenv";
import admin from "firebase-admin";
import serviceAccount from "../serviceAccountKey.json" assert { type: "json" };

dotenv.config();

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: process.env.FIRE_BASE_db_url,
});

const db = admin.firestore();
export { admin, db };
