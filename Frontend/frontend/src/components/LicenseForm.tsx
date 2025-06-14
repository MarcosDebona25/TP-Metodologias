"use client";

import { useState } from "react";
import { Person } from "../types/Person";
import { LicenseSubmission } from "../types/License";
import { submitLicense } from "../services/licenseService";
import PersonSearchField from "./PersonSearchField";
import LicenseTypeSelect from "./LicenseTypeSelect";

export default function LicenseForm() {
  const [formData, setFormData] = useState<{
    person: Person | null;
    selectedLicenseTypes: string[];
  }>({
    person: null,
    selectedLicenseTypes: [],
  });

  const handlePersonFound = (person: Person) => {
    setFormData({ person, selectedLicenseTypes: [] });
  };

  const handleReset = () => {
    setFormData({ person: null, selectedLicenseTypes: [] });
  };

  const handleSubmit = async () => {
    if (!formData.person) {
      alert("Please search for a person first.");
      return;
    }

    const payload: LicenseSubmission = {
      personId: formData.person.id,
      licenseTypes: formData.selectedLicenseTypes,
    };

    try {
      await submitLicense(payload);
      alert("License submitted successfully!");
      handleReset();
    } catch (err: any) {
      console.error(err);
      alert("Error submitting license");
    }
  };

  return (
    <div className="max-w-xl mx-auto p-4 border rounded shadow">
      <h2 className="text-2xl text-gray-800 font-bold mb-4">Create New License</h2>

      <PersonSearchField onPersonFound={handlePersonFound} />

      {formData.person && (
        <>
          <div className="space-y-2 mt-4">
            <div>
              <label className="block font-medium text-gray-700">First Name</label>
              <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={formData.person.firstName} />
            </div>
            <div>
              <label className="block font-medium text-gray-700">Last Name</label>
              <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={formData.person.lastName} />
            </div>
            <div>
              <label className="block font-medium text-gray-700">Date of Birth</label>
              <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={formData.person.dateOfBirth} />
            </div>
            <div>
              <label className="block font-medium text-gray-700">Address</label>
              <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={formData.person.address} />
            </div>
            <div>
              <label className="block font-medium text-gray-700">Blood Type</label>
              <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={formData.person.bloodType} />
            </div>
            <div>
              <label className="block font-medium text-gray-700">Donor</label>
              <input className="w-full rounded text-gray-800 border-gray-300 border-1 bg-gray-300" disabled value={formData.person.donor ? "Yes" : "No"} />
            </div>
          </div>

          <div className="mt-4">
            <LicenseTypeSelect
              value={formData.selectedLicenseTypes}
              onChange={(selected) => setFormData(prev => ({ ...prev, selectedLicenseTypes: selected }))}
              allowedTypes={formData.person.allowedLicenseTypes}
            />
          </div>

          <div className="mt-6 flex justify-end gap-4">
            <button onClick={handleReset} className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600">Reset</button>
            <button onClick={handleSubmit} className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Submit</button>
          </div>
        </>
      )}
    </div>
  );
}

