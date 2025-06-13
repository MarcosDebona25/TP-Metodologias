"use client";

import { useState } from "react";
import PersonSearchField from "../components/PersonSearchField";
import { Person } from "../types/Person";

export default function LicenseForm() {
  const emptyPerson: Person = {
    id: "",
    idNumber: "",
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    address: "",
    bloodType: "",
    donor: false,
  };

  const [formData, setFormData] = useState<Person>(emptyPerson);

  const handlePersonFound = (person: Person) => {
    setFormData(person);
  };

  const handleReset = () => {
    setFormData(emptyPerson);
  };

  return (
    <div className="max-w-lg mx-auto p-6 bg-gray-100 rounded shadow">
      <h1 className="text-2xl font-semibold mb-6 text-gray-800">New License Form</h1>

      <div className="mb-4">
        <PersonSearchField onPersonFound={handlePersonFound} />
      </div>

      <div className="space-y-4">

        <div>
          <label className="block text-sm font-medium text-gray-700">ID Number</label>
          <input
            type="text"
            value={formData.idNumber}
            readOnly
            className="w-full rounded border-gray-300 bg-gray-200 text-gray-700"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700">First Name</label>
          <input
            type="text"
            value={formData.firstName}
            readOnly
            className="w-full rounded border-gray-300 bg-gray-200 text-gray-700"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700">Last Name</label>
          <input
            type="text"
            value={formData.lastName}
            readOnly
            className="w-full rounded border-gray-300 bg-gray-200 text-gray-700"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700">Date of Birth</label>
          <input
            type="text"
            value={formData.dateOfBirth}
            readOnly
            className="w-full rounded border-gray-300 bg-gray-200 text-gray-700"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700">Address</label>
          <input
            type="text"
            value={formData.address}
            readOnly
            className="w-full rounded border-gray-300 bg-gray-200 text-gray-700"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700">Blood Type</label>
          <input
            type="text"
            value={formData.bloodType}
            readOnly
            className="w-full rounded border-gray-300 bg-gray-200 text-gray-700"
          />
        </div>

        <div className="flex items-center gap-2">
          <input
            type="checkbox"
            checked={formData.donor}
            readOnly
            className="rounded border-gray-300"
          />
          <label className="text-sm font-medium text-gray-700">Donor</label>
        </div>

      </div>

      <div className="mt-6 flex justify-end">
        <button
          onClick={handleReset}
          className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
        >
          Reset
        </button>
      </div>
    </div>
  );
}
