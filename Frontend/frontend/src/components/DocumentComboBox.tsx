"use client";

import { useEffect, useState } from "react";
import { Combobox } from "@headlessui/react";
import { CheckIcon, ChevronUpDownIcon } from "@heroicons/react/20/solid";

type Person = {
  id: string;
  idType: string;
  idNumber: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  address: string;
  bloodType: string;
  donor: boolean;
};

type Props = {
  onPersonSelected: (person: Person | null) => void;
};

export default function IDNumberSelect({ onPersonSelected }: Props) {
  const [people, setPeople] = useState<Person[]>([]);
  const [selected, setSelected] = useState<Person | null>(null);
  const [query, setQuery] = useState("");

  useEffect(() => {
    async function fetchPeople() {
      const response = await fetch("/api/people");
      const data = await response.json();
      setPeople(data);
    }
    fetchPeople();
  }, []);

  const filteredPeople =
    query === ""
      ? people
      : people.filter((person) =>
          `${person.idNumber} ${person.firstName} ${person.lastName}`
            .toLowerCase()
            .includes(query.toLowerCase())
        );

  const handleChange = (person: Person | null) => {
    setSelected(person);
    onPersonSelected(person);
  };

  return (
    <div className="w-full">
      <Combobox value={selected} onChange={handleChange}>
        <div className="relative">
          <div className="relative w-full cursor-default overflow-hidden rounded-lg bg-white text-left shadow-md focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-opacity-75 sm:text-sm">
            <Combobox.Input
              className="w-full border-none py-2 pl-3 pr-10 text-sm leading-5 text-gray-900 focus:ring-0"
              displayValue={(person: Person) =>
                person
                  ? `${person.idNumber} - ${person.firstName} ${person.lastName}`
                  : ""
              }
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Search ID number"
            />
            <Combobox.Button className="absolute inset-y-0 right-0 flex items-center pr-2">
              <ChevronUpDownIcon
                className="h-5 w-5 text-gray-400"
                aria-hidden="true"
              />
            </Combobox.Button>
          </div>
          {filteredPeople.length > 0 && (
            <Combobox.Options className="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
              {filteredPeople.map((person) => (
                <Combobox.Option
                  key={person.id}
                  value={person}
                  className={({ active }) =>
                    `relative cursor-default select-none py-2 pl-10 pr-4 ${
                      active ? "bg-blue-600 text-white" : "text-gray-900"
                    }`
                  }
                >
                  {({ selected }) => (
                    <>
                      <span
                        className={`block truncate ${
                          selected ? "font-medium" : "font-normal"
                        }`}
                      >
                        {`${person.idNumber} - ${person.firstName} ${person.lastName}`}
                      </span>
                      {selected && (
                        <span className="absolute inset-y-0 left-0 flex items-center pl-3 text-blue-600">
                          <CheckIcon className="h-5 w-5" aria-hidden="true" />
                        </span>
                      )}
                    </>
                  )}
                </Combobox.Option>
              ))}
            </Combobox.Options>
          )}
        </div>
      </Combobox>
    </div>
  );
}
