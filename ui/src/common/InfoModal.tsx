// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React, { useState } from 'react';
import InfoIcon from '@mui/icons-material/Info';
import {
    Button,
} from 'react-admin';
import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';

const InfoModal = ({ children }): JSX.Element => {
    const [open, setOpen] = useState(false);

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
            <Button onClick={handleClick}>
                <InfoIcon />
            </Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby='info-modal-title'
                aria-describedby='info-modal-description'
            >
                <Box sx={style}>
                    {children}
                </Box>
            </Modal>
        </>
    );
};

export default InfoModal;
