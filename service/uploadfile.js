import { getDownloadURL, getStorage, ref, uploadBytesResumable } from "firebase/storage";
import { firebaseApp } from "./config";

const storage = getStorage(firebaseApp);

const metadata = {
    contentType: 'image/jpeg',
};

export const uploadMultipleFiles = async (files) => {
    const uploadPromises = files.map((file) => {
        return new Promise((resolve, reject) => {
            const storageRef = ref(storage, 'images/' + file.originalname);
            const uploadTask = uploadBytesResumable(storageRef, file.buffer, metadata);

            uploadTask.on(
                'state_changed',
                (snapshot) => {
                    const progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                    console.log(`Upload for ${file.originalname} is ${progress}% done`);
                    switch (snapshot.state) {
                        case 'paused':
                            console.log(`Upload for ${file.originalname} is paused`);
                            break;
                        case 'running':
                            console.log(`Upload for ${file.originalname} is running`);
                            break;
                    }
                },
                (error) => {
                    console.error(`Error uploading ${file.originalname}:`, error);
                    switch (error.code) {
                        case 'storage/unauthorized':
                            console.error('User lacks permission to upload.');
                            break;
                        case 'storage/canceled':
                            console.error('Upload canceled.');
                            break;
                        case 'storage/unknown':
                            console.error('Unknown error occurred.');
                            break;
                    }
                    reject(error);
                },
                () => {
                    getDownloadURL(uploadTask.snapshot.ref)
                        .then((downloadURL) => {
                            console.log(`File available at ${downloadURL}`);
                            resolve({ downloadURL, filename: file.originalname });
                        })
                        .catch((error) => {
                            console.error(`Failed to get download URL for ${file.originalname}:`, error);
                            reject(error);
                        });
                }
            );
        });
    });

    try {
        const results = await Promise.all(uploadPromises);
        return results;
    } catch (error) {
        console.error("Error uploading files:", error);
        throw error;
    }
};
