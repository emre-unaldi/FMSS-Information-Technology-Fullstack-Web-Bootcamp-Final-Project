import { NextRequest, NextResponse } from "next/server";
import axios from "axios";

interface AddressComponent {
    long_name: string;
    short_name: string;
    types: string[];
}

interface Location {
    lat: number;
    lng: number;
}

interface Geometry {
    bounds: {
        northeast: Location;
        southwest: Location;
    };
    location: Location;
    location_type: string;
    viewport: {
        northeast: Location;
        southwest: Location;
    };
}

interface Result {
    address_components: AddressComponent[];
    formatted_address: string;
    geometry: Geometry;
    partial_match: boolean;
    place_id: string;
    types: string[];
}

interface GeocodeResponse {
    results: Result[];
    status: string;
}

const GET = async (request: NextRequest) => {
    const { searchParams } = new URL(request.url);
    const address = searchParams.get('address');
    const apiKey = process.env.NEXT_PUBLIC_GOOGLE_API_KEY;

    if (!address) {
        return NextResponse.json(
            {
                success: false,
                message: 'No address found for Geocode',
                responseDateTime: new Date().getTime(),
                data: {
                    error: "Address parameter is required"
                }
            },
            { status: 400 }
        );
    }

    try {
        const response = await axios.get<GeocodeResponse>(
            `https://maps.googleapis.com/maps/api/geocode/json`,
            {
                params: {
                    address,
                    key: apiKey
                }
            }
        );

        if (response.data.status !== "OK") {
            return NextResponse.json(
                {
                    success: false,
                    message: 'Google Geocoding API request failed',
                    responseDateTime: new Date().getTime(),
                    data: {
                        error: "Google Geocoding API request failed. Check API key or address"
                    }
                },
                { status: 400 }
            );
        }

        const location = response.data.results[0].geometry.location;
        return NextResponse.json(
            {
                success: true,
                message: 'Google Geocoding API request successful. Address location found',
                responseDateTime: new Date().getTime(),
                data: {
                    location: location
                }
            },
            { status: 200 }
        );

    } catch (error) {
        if (axios.isAxiosError(error)) {
            return NextResponse.json(
                {
                    success: false,
                    message: 'A server error occurred while making a Google Geocoding API request',
                    responseDateTime: new Date().getTime(),
                    data: {
                        error: error.message
                    }
                },
                { status: 500 }
            );
        }

        return NextResponse.json(
            {
                success: false,
                message: 'An unknown error occurred',
                responseDateTime: new Date().getTime(),
                data: {
                    error: String(error)
                }
            },
            { status: 500 }
        );
    }
}

export { GET };
