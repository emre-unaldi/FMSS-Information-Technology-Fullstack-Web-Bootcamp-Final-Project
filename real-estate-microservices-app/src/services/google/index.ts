interface Location {
    lat: number;
    lng: number;
}

const getLocation = async (address: string): Promise<Location> => {
    try {
        const response = await fetch(`/api/geocode?address=${encodeURIComponent(address)}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        const { success, message, data: responseData } = data;

        if (!success) {
            new Error(message || "Google Geocoding API request failed. Check the address.");
        }

        const { location } = responseData;

        return location;
    } catch (error) {
        throw new Error(error instanceof Error ? error.message : String(error));
    }
}

export { getLocation };
export type { Location };
