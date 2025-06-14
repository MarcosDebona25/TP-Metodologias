// pages/api/people.ts
import { NextResponse } from "next/server";
export async function GET() {
    const dummyPeople = 
{
  "id": "1",
  "idNumber": "12345678",
  "firstName": "Juan",
  "lastName": "Pérez",
  "dateOfBirth": "1990-04-10",
  "address": "123 Main St",
  "bloodType": "A+",
  "donor": true,
  "allowedLicenseTypes": ["A", "B", "C"]
}
  ;
  return NextResponse.json(dummyPeople);
}
