import { db } from "../configs/firebase-config.js";
import { UserRole } from "../utils/enums.js";

export const addOfficer = async () => {
    const docRef = db.collection("users");
    const emailToFind = "officer@example.com";
    const snapshot = await docRef.where("email", "==", emailToFind).get();
    if (snapshot.empty) {
        await docRef.doc().set({
            name: "Officer Name",
            email: emailToFind,
            role: UserRole.OFFICER,
            phone: "1234567890",
            address: "123 Officer St, City, Country",
            dateOfBirth: "1990-01-01",
            gender: "Male",
            joiningDate: "2023-01-01",
        });
        console.log("User inserted with ID:", docRef.id);
    }
};

const schools = [
    {
        mandal: "Mandal A",
        district: "District A",
        state: "State A",
        country: "Country A",
        pincode: "123456",
        schoolType: "Government",
        schoolCategory: "Primary",
        schoolMedium: "Telugu",
        schoolManagement: "Government",
        schoolLevel: "Primary",
        schoolCode: "SCH001",
        schoolName: "School A",
        schoolAddress: "123 School St, City, Country",
        schoolPhone: "1234567890",
        schoolEmail: "schoolA@example.com"
    },
    {
        mandal: "Mandal B",
        district: "District B",
        state: "State B",
        country: "Country B",
        pincode: "654321",
        schoolType: "Private",
        schoolCategory: "Secondary",
        schoolMedium: "English",
        schoolManagement: "Private",
        schoolLevel: "Secondary",
        schoolCode: "SCH002",
        schoolName: "School B",
        schoolAddress: "456 School St, City, Country",
        schoolPhone: "9876543210",
        schoolEmail: "schoolB@example.com"
    },
    {
        mandal: "Mandal C",
        district: "District C",
        state: "State C",
        country: "Country C",
        pincode: "789012",
        schoolType: "Government",
        schoolCategory: "Higher Secondary",
        schoolMedium: "Hindi",
        schoolManagement: "Government",
        schoolLevel: "Higher Secondary",
        schoolCode: "SCH003",
        schoolName: "School C",
        schoolAddress: "789 School St, City, Country",
        schoolPhone: "4567891230",
        schoolEmail: "schoolC@example.com"
    },
    {
        mandal: "Mandal D",
        district: "District D",
        state: "State D",
        country: "Country D",
        pincode: "345678",
        schoolType: "Private",
        schoolCategory: "Primary",
        schoolMedium: "Telugu",
        schoolManagement: "Private",
        schoolLevel: "Primary",
        schoolCode: "SCH004",
        schoolName: "School D",
        schoolAddress: "321 School St, City, Country",
        schoolPhone: "3216549870",
        schoolEmail: "schoolD@example.com"
    }
];

export const addSchools = async () => {
    const docRef = db.collection("schools");
    schools.forEach(async (school) => {
        const schoolDoc = docRef.doc();
        const snapshot = await docRef.where("schoolName", "==", school.schoolName).get();
        if (snapshot.empty) {
            await schoolDoc.set(school);
            console.log("School inserted with ID:", schoolDoc.id);
        } else {
            console.log(`School with name ${school.schoolName} already exists.`);
        }
    });
}