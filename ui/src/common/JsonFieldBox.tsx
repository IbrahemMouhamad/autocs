// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React, { Children } from 'react';
import Box from '@mui/material/Box';

import JsonFieldItem from './JsonFieldItem';

const JsonFieldBox = ({
    record,
    resource,
    children,
    ...rest
}): JSX.Element => (
    <Box {...rest}>
        {Children.map(
            children,
            (child) => (
                <JsonFieldItem
                    field={child}
                    record={record}
                    resource={resource}
                />
            ),
        )}
    </Box>
);

export default JsonFieldBox;
