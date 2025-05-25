export interface User {
    name: string;
    email: string;
    role: string;
    class: string;
    password: string;
    phone: string;
    address: string;
    dateOfBirth: string;
    motherName: string;
    fatherName: string;
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

export interface Commodity {
    title: string;
    phase1: number;
    phase2: number;
}

export interface Annocement {
    title: string;
    description: string;
    date: string;
    schoolId: string;
}

export interface Attendance {
    date: string;
    schoolId: string;
    userId: string;
    class: string;
    present: boolean;
}

export interface Marks{
    schoolId: string;
    userId: string;
    class: string;
    marks: number;
}