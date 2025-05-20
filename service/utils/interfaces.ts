export interface User {
    name: string;
    email: string;
    role: string;
    password: string;
    phone: string;
    address: string;
    dateOfBirth: string;
    gender: "Male" | "Female" | "Other";
    joiningDate: string;
}

export interface School {
    mandal: string;
    district: string;
    state: string;
    country: string;
    pincode: string;
    schoolType: string;
    schoolCategory: string;
    schoolMedium: string;
    schoolManagement: string;
    schoolLevel: string;
    schoolCode: string;
    schoolName: string;
    schoolAddress: string;
    schoolPhone: string;
    schoolEmail: string;
}

export interface SchoolUserMapping {
    schoolId: string;
    userId: string;
    role: string;
}