// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Button,
    useTranslate,
} from 'react-admin';
import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';

const DefaultModal = ({
    children,
    icon,
    action,
    open,
    setOpen,
}): JSX.Element => {
    const translate = useTranslate();

    const handleClick = (): void => {
        setOpen(true);
    };

    const handleClose = (): void => {
        setOpen(false);
    };

    const style = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        backgroundColor: 'background.paper',
        border: '2px solid #000',
        boxShadow: 24,
        p: 4,
    };

    return (
        <>
            <Button
                onClick={handleClick}
                title={translate(action)}
            >
                {icon}
            </Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby={`${translate(action)}-modal-title`}
                aria-describedby={`${translate(action)}info-modal-description`}
            >
                <Box sx={style}>
                    {children}
                </Box>
            </Modal>
        </>
    );
};

export default DefaultModal;
