"use client";

import { useState } from "react";
import { fetchExpiringLicenses } from "@/services/licenseService";
import { LicenciaActiva } from "@/types/License";

type SortField =
  | "documentoTitular"
  | "clases"
  | "fechaEmisionLicencia"
  | "fechaVencimientoLicencia";
type SortDirection = "asc" | "desc";

export default function ExpiringLicensesTable() {
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");
  const [results, setResults] = useState<LicenciaActiva[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const [sortField, setSortField] = useState<SortField>(
    "fechaVencimientoLicencia"
  );
  const [sortDirection, setSortDirection] = useState<SortDirection>("asc");

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
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message || "Error fetching data");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleSort = (field: SortField) => {
    if (field === sortField) {
      // toggle direction
      setSortDirection((prev) => (prev === "asc" ? "desc" : "asc"));
    } else {
      setSortField(field);
      setSortDirection("asc");
    }
  };

  const sortedResults = [...results].sort((a, b) => {
    const aVal = a[sortField];
    const bVal = b[sortField];

    return sortDirection === "asc"
      ? String(aVal).localeCompare(String(bVal))
      : String(bVal).localeCompare(String(aVal));
  });

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
        Licencias a Expirar
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

      {sortedResults.length > 0 ? (
        <div className="overflow-x-auto">
          <table className="min-w-full border-gray-500 border-2">
            <thead className="bg-blue-500 text-white">
              <tr>
                {[
                  { label: "Titular", field: "numeroDocumento" },
                  { label: "Clase/s", field: "clases" },
                  { label: "Fecha de emisión", field: "fechaEmision" },
                  { label: "Fecha de vencimiento", field: "fechaVencimiento" },
                ].map(({ label, field }) => (
                  <th
                    key={field}
                    onClick={() => handleSort(field as SortField)}
                    className="cursor-pointer max-w-2.5 py-2 border border-gray-500"
                  >
                    {label}
                    {sortField === field &&
                      (sortDirection === "asc" ? " ▲" : " ▼")}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {sortedResults.map((license, index) => (
                <tr key={index} className="text-center">
                  <td className="border text-gray-800 border-gray-500 bg-gray-200 px-4 py-2">
                    {license.documentoTitular}
                  </td>
                  <td className="border text-gray-800 border-gray-500 bg-gray-200 px-4 py-2">
                    {license.clases}
                  </td>
                  <td className="border text-gray-800 border-gray-500 bg-gray-200 px-4 py-2">
                    {license.fechaEmisionLicencia}
                  </td>
                  <td className="border text-gray-800 border-gray-500 bg-gray-200 px-4 py-2">
                    {license.fechaVencimientoLicencia}
                  </td>
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
