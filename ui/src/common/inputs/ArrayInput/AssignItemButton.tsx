// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import * as React from 'react';
import { useSimpleFormIterator, Button, ButtonProps } from 'react-admin';

const AssignItemButton = (props: Omit<ButtonProps, 'onClick'>): JSX.Element => {
    const { add } = useSimpleFormIterator();
    const { label, icon } = props;

    return (
        <Button label={label} onClick={() => add()} {...props}>
            {icon}
        </Button>
    );
};

export default AssignItemButton;
