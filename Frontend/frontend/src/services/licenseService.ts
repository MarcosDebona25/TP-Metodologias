type LicenseSubmission = {
  personId: string;
  licenseTypes: string[];
};

export async function submitLicense(data: LicenseSubmission): Promise<void> {
  const response = await fetch("/api/licenses", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error("Failed to submit license");
  }
}
