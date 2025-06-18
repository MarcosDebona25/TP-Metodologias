"use client";

import Select from "react-select";

const LICENSE_TYPES = ["A", "B", "C", "D", "E", "F", "G"];

type Props = {
  value: string[];
  onChange: (selected: string[]) => void;
  currentTypes?: string[];
  allowedTypes?: string[];
  currentTypesWarnings: boolean;
};

export default function LicenseTypeSelect({
  value,
  onChange,
  currentTypes,
  allowedTypes,
  currentTypesWarnings,
}: Props) {
  const options = LICENSE_TYPES.map((type) => ({ value: type, label: type }));

  const handleChange = (selectedOptions: any) => {
    const selectedValues = selectedOptions
      ? selectedOptions.map((opt: any) => opt.value)
      : [];
    onChange(selectedValues);
  };

  return (
    <div className="space-y-2">
      <label className="block font-medium text-gray-700">
        Clase/s Solicitada/s
      </label>
      {currentTypes && (
        <p className="text-sm mt-2 text-gray-700">
          Tiene licencia de clase:{" "}
          <strong>{currentTypes.join(", ") || "ninguna"}</strong>.
        </p>
      )}
      {allowedTypes && (
        <p className="text-sm mt-2 text-gray-700">
          No puede solicitar:{" "}
          <strong>
            {["A", "B", "C", "D", "E", "F", "G"]
              .filter((t) => !allowedTypes.includes(t))
              .join(", ") || "ninguna"}
          </strong>
          .
        </p>
      )}
      <Select
        options={options}
        isMulti
        value={options.filter((opt) => value.includes(opt.value))}
        onChange={handleChange}
        placeholder="Seleccione las clases a solicitar"
        className="text-black"
      />
      {currentTypes &&
        currentTypesWarnings &&
        value.some((t) => currentTypes.includes(t)) && (
          <p className="text-yellow-600 text-sm mt-1">
            El/la solicitante ya posee una o más de las clases seleccionadas.
          </p>
        )}
      {value.map((type) =>
        allowedTypes && !allowedTypes.includes(type) ? (
          <div key={type} className="text-sm text-red-600">
            ⚠ El titular no satisface los requisitos para solicitar una licencia
            clase {type}
          </div>
        ) : null
      )}
    </div>
  );
}
