const uploadPhotos = async (photos: FormData, accessToken: string) => {
    try {
        const response = await fetch("http://localhost:8080/api/v1/photos/uploads", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${accessToken}`
            },
            body: photos,
            credentials: "include"
        });

        if (!response.ok) {
            new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        throw new Error(error instanceof Error ? error.message : String(error));
    }
};

export { uploadPhotos };