// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    SelectInput as RASelectInput,
} from 'react-admin';

import InputHelpIcon from '../InputHelpTooltip';

const SelectInput = ({
    helpText,
    isTranslatable = false,
    ...rest
}): JSX.Element => (
    <RASelectInput
        InputProps={{
            startAdornment: (
                <InputHelpIcon position='start' text={helpText} isTranslatable={isTranslatable} />
            ),
        }}
        variant='outlined'
        resettable
        fullWidth
        {...rest}
    />
);

export default SelectInput;
