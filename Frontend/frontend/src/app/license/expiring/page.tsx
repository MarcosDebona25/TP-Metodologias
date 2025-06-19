// app/license/expiring/page.tsx
import ExpiringLicensesTable from "@/components/ExpiringLicensesTable";

export default function ExpiringLicensesPage() {
  return (
    <div className="p-6">
      <ExpiringLicensesTable />
    </div>
  );
}
