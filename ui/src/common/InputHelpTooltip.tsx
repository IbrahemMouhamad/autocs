// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import Tooltip from '@mui/material/Tooltip';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';
import InputAdornment from '@mui/material/InputAdornment';
import { useTranslate } from 'react-admin';

export const IconWithTooltip = ({ helpText }): JSX.Element => (
    <Tooltip title={helpText}>
        <InfoOutlinedIcon />
    </Tooltip>
);

const InputHelpIcon = ({ position, text, isTranslatable = false }): JSX.Element => {
    const translate = useTranslate();

    return (
        <InputAdornment position={position}>
            <IconWithTooltip helpText={isTranslatable ? translate(text) : text} />
        </InputAdornment>
    );
};

export default InputHelpIcon;
