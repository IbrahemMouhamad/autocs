// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    TextInput as RATextInput,
} from 'react-admin';

import InputHelpIcon from '../InputHelpTooltip';

const TextInput = ({
    source,
    helpText,
    isTranslatable = false,
    ...rest
}): JSX.Element => (
    <RATextInput
        source={source}
        InputProps={{
            endAdornment: (
                <InputHelpIcon position='end' text={helpText} isTranslatable={isTranslatable} />
            ),
        }}
        resettable
        variant='outlined'
        fullWidth
        {...rest}
    />
);

export default TextInput;
