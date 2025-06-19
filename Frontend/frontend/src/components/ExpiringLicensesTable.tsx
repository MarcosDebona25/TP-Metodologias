"use client";

import { useState } from "react";
import { fetchExpiringLicenses } from "@/services/licenseService";
import { LicenseSummary } from "@/types/LicenseSummary";

export default function ExpiringLicensesTable() {
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");
  const [results, setResults] = useState<LicenseSummary[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSearch = async () => {
    if (!from || !to) return;

    const fromDate = new Date(from);
    const toDate = new Date(to);
    const diffDays =
      (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24);

    if (diffDays < 0) {
      setError("La fecha final no puede ser anterior a la inicial.");
      return;
    }

    if (diffDays > 7) {
      setError("El rango de fechas no puede superar una semana.");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const data = await fetchExpiringLicenses(from, to);
      setResults(data);
    } catch (err: any) {
      setError(err.message || "Error fetching data");
    } finally {
      setLoading(false);
    }
  };
  let validRange = true;
  if (from && to) {
    const fromDate = new Date(from);
    const toDate = new Date(to);
    const diffDays =
      (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24);
    if (diffDays > 7 || diffDays < 0) {
      validRange = false;
    }
  }
  return (
    <div className="max-w-4xl mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4 text-gray-800">
        Licencias Vencidas o por Vencer
      </h1>

      <div className="flex gap-4 items-end mb-6">
        <div>
          <label className="block text-lg font-medium text-gray-700">
            Desde
          </label>
          <input
            type="date"
            value={from}
            onChange={(e) => setFrom(e.target.value)}
            className="w-full h-8 rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
          />
        </div>
        <div>
          <label className="block text-lg font-medium text-gray-700">
            Hasta
          </label>
          <input
            type="date"
            value={to}
            onChange={(e) => setTo(e.target.value)}
            className="w-full h-8 rounded text-gray-800 border-gray-300 border-1 bg-gray-300"
          />
        </div>
        <button
          onClick={handleSearch}
          disabled={!from || !to || !validRange}
          className="bg-blue-600 text-white px-2 py-1 h-8 rounded hover:bg-blue-700 disabled:opacity-50"
        >
          Buscar
        </button>
      </div>

      {!validRange && (
        <p className="text-red-500 mb-4">
          El rango de fechas debe ser de entre 0 y 7 días
        </p>
      )}
      {error && <p className="text-red-500 mb-4">{error}</p>}

      {results.length > 0 ? (
        <div className="overflow-x-auto">
          <table className="min-w-full border border-gray-300">
            <thead className="bg-gray-100">
              <tr>
                <th className="px-4 py-2 border">Titular</th>
                <th className="px-4 py-2 border">Clase/s</th>
                <th className="px-4 py-2 border">Fecha de emisión</th>
                <th className="px-4 py-2 border">Fecha de vencimiento</th>
              </tr>
            </thead>
            <tbody>
              {results.map((license, index) => (
                <tr key={index} className="text-center">
                  <td className="px-4 py-2 border">{license.idNumber}</td>
                  <td className="px-4 py-2 border">
                    {license.licenseTypes.join(", ")}
                  </td>
                  <td className="px-4 py-2 border">{license.grantDate}</td>
                  <td className="px-4 py-2 border">{license.expirationDate}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        !loading && (
          <p className="text-gray-600">No se encontraron licencias.</p>
        )
      )}
    </div>
  );
}
