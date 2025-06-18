"use client";

import { useState } from "react";
import { z } from "zod";
import { Person } from "../types/Person";

type Props = {
  onPersonFound: (person: Person) => void;
  getPersonByIdNumber: (id: string) => Promise<Person>;
};

const idSchema = z
  .string()
  .length(8, "DNI debe tener 8 dígitos de longitud")
  .regex(/^\d+$/, "DNI debe contener solo dígitos numéricos");

export default function PersonSearchField({
  onPersonFound,
  getPersonByIdNumber,
}: Props) {
  const [idNumber, setIdNumber] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [validationError, setValidationError] = useState<string | null>(null);

  const fetchPerson = async () => {
    const result = idSchema.safeParse(idNumber);
    if (!result.success) {
      setValidationError(result.error.errors[0].message);
      return;
    }

    setValidationError(null);
    setLoading(true);
    setError(null);

    try {
      const data = await getPersonByIdNumber(idNumber);
      onPersonFound(data);
    } catch (err: any) {
      setError(err.message || "Error al buscar persona");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-2">
      <label className="block font-medium text-gray-700">DNI</label>
      <div className="flex gap-2">
        <input
          type="text"
          value={idNumber}
          onChange={(e) => setIdNumber(e.target.value)}
          placeholder="Ingrese DNI"
          className='w-full text-black rounded border-grey-800 border-1 ${
            validationError || error ? "border-red-500" : "border-gray-300"
          } focus:ring-blue-500'
        />
        <button
          onClick={fetchPerson}
          disabled={loading}
          className="px-4 py-2 bg-blue-600 text-white rounded disabled:opacity-50"
        >
          {loading ? (
            <svg className="animate-spin h-5 w-5 mx-auto" viewBox="0 0 24 24">
              <circle
                className="opacity-25"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                strokeWidth="4"
              ></circle>
              <path
                className="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8v8z"
              ></path>
            </svg>
          ) : (
            "Buscar"
          )}
        </button>
      </div>
      {validationError && (
        <div className="text-red-500 text-sm">{validationError}</div>
      )}
      {error && !validationError && (
        <div className="text-red-500 text-sm">{error}</div>
      )}
    </div>
  );
}
