// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    NumberInput as RANumberInput,
} from 'react-admin';

import InputHelpIcon from '../InputHelpTooltip';

const NumberInput = ({
    source,
    helpText,
    isTranslatable = false,
    ...rest
}): JSX.Element => (
    <RANumberInput
        source={source}
        InputProps={{
            endAdornment: (
                <InputHelpIcon position='end' text={helpText} isTranslatable={isTranslatable} />
            ),
        }}
        variant='outlined'
        fullWidth
        {...rest}
    />
);

export default NumberInput;
