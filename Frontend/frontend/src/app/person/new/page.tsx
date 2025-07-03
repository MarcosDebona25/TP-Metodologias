import NewPersonForm from "@/components/NewPersonForm";
import ProtectedRoute from "@/components/ProtectedRoute";

export default function NewPersonPage() {
  return (
    <ProtectedRoute>
      <NewPersonForm />
    </ProtectedRoute>
  );
}
