// app/license/expiring/page.tsx
import ExpiringLicensesTable from "@/components/ExpiringLicensesTable";
import ProtectedRoute from "@/components/ProtectedRoute";

export default function ExpiringLicensesPage() {
  return (
    <ProtectedRoute>
      <ExpiringLicensesTable />
    </ProtectedRoute>
  );
}
