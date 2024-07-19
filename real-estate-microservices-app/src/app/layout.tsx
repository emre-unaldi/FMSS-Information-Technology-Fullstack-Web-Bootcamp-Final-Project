import React from "react";
import type {Metadata} from "next";
import {Inter} from "next/font/google";
import {AntdRegistry} from '@ant-design/nextjs-registry';
import "./globals.css";

const inter = Inter({subsets: ["latin"]});

export const metadata: Metadata = {
    title: "Real Estate App",
    description: "Real Estate Microservices Application"
};

export default function RootLayout({children}: Readonly<{ children: React.ReactNode }>) {
    return (
        <html lang="en">
            <body className={inter.className}>
                <AntdRegistry>
                    {children}
                </AntdRegistry>
            </body>
        </html>
    );
}