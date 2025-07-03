import ProtectedRoute from "@/components/ProtectedRoute";
import UpdatePersonForm from "@/components/UpdatePersonForm";

export default function UpdatePersonPage() {
  return (
    <ProtectedRoute>
      <UpdatePersonForm />
    </ProtectedRoute>
  );
}
