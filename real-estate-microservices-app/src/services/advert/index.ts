interface IAdvert {
    "photoIds": string[],
    "userId": number,
    "housingType": string,
    "advertType": string,
    "title": string,
    "description": string,
    "addressId": number,
    "releaseDate": string,
    "validityDate": string,
    "area": number,
    "numberOfRooms": number,
    "price": number,
    "isBalcony": boolean,
    "isCarPark": boolean
}

const createAdvert = async (data: IAdvert, accessToken: string) => {
    try {
        const response = await fetch("http://localhost:8080/api/v1/adverts", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${accessToken}`
            },
            body: JSON.stringify(data),
            credentials: "include"
        })

        if (!response.ok) {
            new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        throw new Error(error instanceof Error ? error.message : String(error));
    }
}

export { createAdvert }
export type { IAdvert }