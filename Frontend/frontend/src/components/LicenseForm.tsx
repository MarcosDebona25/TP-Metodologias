"use client";

import { useState } from "react";
import IDNumberSelect from "./DocumentComboBox";

export default function LicenseForm() {
  const [idType, setIdType] = useState("dni");
  const [dateOfBirth, setDateOfBirth] = useState("");

  return (
    <form className="space-y-6 max-w-2xl">
      <h1 className="text-2xl font-bold mb-2">Create New License</h1>

      {/* ID Type + ID Number */}
      <div>
        <label className="block font-medium mb-1">Identification</label>
        <div className="flex gap-4">
          {/* ID Type Dropdown */}
          <div className="w-32">
            <select
              value={idType}
              onChange={(e) => setIdType(e.target.value)}
              className="w-full border rounded px-3 py-2"
            >
              <option value="dni">DNI</option>
              <option value="passport">Passport</option>
              <option value="cuil">CUIL</option>
            </select>
          </div>

          {/* ID Number Combobox */}
          <div className="flex-1">
            <IDNumberSelect
              onPersonSelected={(person) => {
                setDateOfBirth(person.dateOfBirth);
                // You can also fill other fields here
              }}
            />
          </div>
        </div>
      </div>

      {/* Date of Birth */}
      <div>
        <label className="block font-medium mb-1">Date of Birth</label>
        <input
          type="date"
          value={dateOfBirth}
          onChange={(e) => setDateOfBirth(e.target.value)}
          className="w-full border rounded px-3 py-2"
        />
      </div>

      {/* Submit Button */}
      <button
        type="submit"
        className="bg-blue-600 text-white px-4 py-2 rounded"
      >
        Submit
      </button>
    </form>
  );
}
