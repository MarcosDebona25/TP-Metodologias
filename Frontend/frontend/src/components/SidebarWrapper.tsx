"use client";

import Sidebar from "./Sidebar";
import { getTokenData } from "@/lib/auth";
import { useEffect, useState } from "react";

export default function SidebarWrapper() {
  const [role, setRole] = useState<string | null>(null);

  useEffect(() => {
    const data = getTokenData();
    if (data?.rol) {
      setRole(data.rol);
    }
  }, []);

  if (!role) return null;
  console.log(role);
  return <Sidebar role={role} />;
}
