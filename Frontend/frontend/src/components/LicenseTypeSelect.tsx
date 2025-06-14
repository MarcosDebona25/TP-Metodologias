"use client";

import Select from "react-select";

const LICENSE_TYPES = ["A", "B", "C", "D", "E", "F", "G"];

type Props = {
  value: string[];
  onChange: (selected: string[]) => void;
  allowedTypes?: string[]; // optional for warnings
};

export default function LicenseTypeSelect({ value, onChange, allowedTypes }: Props) {
  const options = LICENSE_TYPES.map(type => ({ value: type, label: type }));

  const handleChange = (selectedOptions: any) => {
    const selectedValues = selectedOptions ? selectedOptions.map((opt: any) => opt.value) : [];
    onChange(selectedValues);
  };

  return (
    <div className="space-y-2">
      <label className="block font-medium text-gray-700">Clase/s Solicitada/s</label>
      <Select
        options={options}
        isMulti
        value={options.filter(opt => value.includes(opt.value))}
        onChange={handleChange}
        placeholder="Seleccione las clases a solicitar"
        className="text-black"
      />

      {value.map(type => (
        allowedTypes && !allowedTypes.includes(type) ? (
          <div key={type} className="text-sm text-red-600">
            ⚠ El titular no satisface los requisitos para solicitar una licencia clase {type}
          </div>
        ) : null
      ))}
    </div>
  );
}
