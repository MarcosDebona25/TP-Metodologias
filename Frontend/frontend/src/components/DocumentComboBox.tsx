"use client";

import Select from "react-select";
import { useEffect, useState } from "react";

type Person = {
  id: string;
  idNumber: string;
  fullName: string;
  dateOfBirth: string;
};

type Props = {
  onPersonSelected: (person: Person) => void;
};

export default function IDNumberSelect({ onPersonSelected }: Props) {
  const [people, setPeople] = useState<Person[]>([]);
  const [selected, setSelected] = useState<any>(null);

  useEffect(() => {
    // Simulated fetch from backend (replace with real API call)
    async function fetchPeople() {
      const response = await fetch("/api/people"); // your real endpoint
      const data = await response.json();
      setPeople(data);
    }

    fetchPeople();
  }, []);

  const options = people.map((person) => ({
    value: person.id,
    label: `${person.idNumber} - ${person.fullName}`,
    person,
  }));

  const handleChange = (option: any) => {
    setSelected(option);
    onPersonSelected(option?.person);
  };

  return (
    <Select
      options={options}
      onChange={handleChange}
      value={selected}
      placeholder="Search ID number"
      isClearable
      className="w-full"
    />
  );
}
