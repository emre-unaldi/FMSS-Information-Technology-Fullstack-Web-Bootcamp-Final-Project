"use client"
import React, {useState} from 'react';
import {Button, message, Upload, UploadProps} from 'antd';
import {UploadOutlined} from '@ant-design/icons';
import type {UploadFile} from 'antd/es/upload/interface';
import Cookies from "universal-cookie";
import {uploadPhotos} from '@/services/photo';

const PhotoUploader: React.FC = () => {
    const [photos, setPhotos] = useState<UploadFile[]>([]);
    const [messageApi, contextHolder] = message.useMessage();
    const key = 'uploads';
    const cookies = new Cookies();

    const handleUpload = async () => {
        messageApi.open({
            key,
            type: 'loading',
            content: 'Photos are loading...',
        });

        const formData: FormData = new FormData();
        photos.forEach((photo) => {
            formData.append('photos', photo as any);
        });

        const accessToken = await cookies.get("jwt-access-token");

        try {
            const response = await uploadPhotos(formData, accessToken);

            if (response?.success) {
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'success',
                        content: 'Photos uploaded successfully',
                        duration: 2,
                    });
                }, 1000);
                setPhotos([]);
            } else {
                setTimeout(() => {
                    messageApi.open({
                        key,
                        type: 'error',
                        content: 'Failed to upload photos',
                        duration: 2,
                    });
                }, 1000);
            }
        } catch (error) {
            setTimeout(() => {
                messageApi.open({
                    key,
                    type: 'error',
                    content: 'An error occurred while uploading photos',
                    duration: 2,
                });
            }, 1000);
        }
    };

    const photoUploaderProps: UploadProps = {
        name: 'photos',
        multiple: true,
        onChange: ({file, fileList}) => {
            console.log(file, fileList);
        },
        onDrop: (e) => {
            console.log('Dropped files:', e.dataTransfer.files);
        },
        beforeUpload: (file) => {
            setPhotos(prevList => [...prevList, file]);
            return false;
        },
        onRemove: (file) => {
            setPhotos(prevList => prevList.filter(item => item.uid !== file.uid));
        },
    };

    return (
        <>
            {contextHolder}
            <div style={{width: '50%', height: '50vh', margin: '50px'}}>
                <Upload.Dragger
                    listType="picture"
                    showUploadList={true}
                    {...photoUploaderProps}
                >
                    <p className="ant-upload-drag-icon">
                        <UploadOutlined/>
                    </p>
                    <p className="ant-upload-text">Click or drag file to this area to upload</p>
                    <p className="ant-upload-hint">Supports: JPEG, JPG, PNG, GIF, BMP, WEBP, TIFF, SVG+XML</p>
                </Upload.Dragger>
                <Button
                    type="primary"
                    onClick={handleUpload}
                    disabled={photos.length === 0}
                    style={{marginTop: 16}}
                >
                    {photos.length > 1 ? 'Upload Photos' : 'Upload Photo'}
                </Button>
            </div>
        </>
    );
};

export default PhotoUploader;
