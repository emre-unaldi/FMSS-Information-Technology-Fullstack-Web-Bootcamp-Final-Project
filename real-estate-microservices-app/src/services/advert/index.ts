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

interface IAddress {
    id: number;
    neighborhood: string;
    street: string;
    province: string;
    county: string;
    zipCode: string;
}

interface IUser {
    id: number;
    firstName: string;
    lastName: string;
    username: string;
    email: string;
    password: string;
    phoneNumber: string;
    roles: string[];
}

interface IPhoto {
    id: string;
    name: string;
    downloadUrl: string;
    type: string;
    size: number;
}

interface IAdvertResponse {
    id: number;
    advertNumber: string;
    photos: IPhoto[];
    user: IUser;
    housingType: string;
    advertStatus: string;
    advertType: string;
    title: string;
    description: string;
    address: IAddress;
    releaseDate: string;
    validityDate: string;
    area: number;
    numberOfRooms: number;
    price: number;
    isBalcony: boolean;
    isCarPark: boolean;
}

interface IAdvertsApiResponse {
    success: boolean;
    message: string;
    responseDateTime: string;
    data: IAdvertResponse[];
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

const fetchAdverts = async (accessToken: string) => {
    try {
        const response = await fetch("http://localhost:8080/api/v1/adverts", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${accessToken}`
            },
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

const fetchAdvert = async (advertId: number, accessToken: string) => {
    try {
        const response = await fetch(`http://localhost:8080/api/v1/adverts/${advertId}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${accessToken}`
            },
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

const deleteAdvert = async (advertId: number, accessToken: string) => {
    try {
        const response = await fetch(`http://localhost:8080/api/v1/adverts/${advertId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${accessToken}`
            },
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

export { createAdvert, fetchAdverts, fetchAdvert, deleteAdvert }
export type { IAdvert, IAddress, IUser, IPhoto, IAdvertResponse, IAdvertsApiResponse }