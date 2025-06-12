"use client";

import { useState } from "react";
import IDNumberSelect from "./DocumentComboBox";

export default function LicenseForm() {
  const [lastName, setLastName] = useState("");
  const [firstName, setFirstName] = useState("");
  const [dateOfBirth, setDateOfBirth] = useState("");
  const [address, setAddress] = useState("");
  const [bloodType, setBloodType] = useState("");

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
              className="w-full border rounded px-3 py-2"
            >
              <option value="">Seleccione</option>
              <option value="DNI">DNI</option>
              <option value="LE/LI">LE/LC</option>
              <option value="CI">CI</option>
            </select>
          </div>

          {/* ID Number Combobox */}
          <div className="flex-1">
            <IDNumberSelect
              onPersonSelected={(person) => {
                setLastName(person.lastName);
                setFirstName(person.firstName);
                setDateOfBirth(person.dateOfBirth);
                setAddress(person.address);
                setBloodType(person.bloodType);
                // You can also fill other fields here
              }}
            />
          </div>
        </div>
      </div>

        <div>
          <label className="block font-medium">Apellido</label>
          <input type="text" value={lastName} onChange={(t) => setLastName(t.target.value)} className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-medium">Nombre</label>
          <input type="text" value={firstName} onChange={(t) => setFirstName(t.target.value)} className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-medium">Fecha de Nacimiento</label>
          <input type="date" value={dateOfBirth} onChange={(t) => setDateOfBirth(t.target.value)} className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-medium">Dirección</label>
          <input type="text" value={address} onChange={(t) => setAddress(t.target.value)} className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-medium">
            Grupo Sanguíneo y Factor RH
          </label>
          <input type="text" value={bloodType} onChange={(t) => setBloodType(t.target.value)} className="w-full border rounded px-3 py-2" />
        </div>
        <button
          type="submit"
          className="bg-blue-600 text-white px-4 py-2 rounded"
        >
          Submit
        </button>
    </form>
  );
}
