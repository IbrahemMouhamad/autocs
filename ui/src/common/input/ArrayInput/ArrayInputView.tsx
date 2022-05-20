// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import { makeStyles } from '@mui/styles';
import {
    ArrayInput,
    SimpleFormIterator,
    useTranslate,
} from 'react-admin';
import AddLinkIcon from '@mui/icons-material/AddLink';

import AssignItemButton from './AssignItemButton';

export const useArrayInputViewStyles = makeStyles(
    () => ({
        input: {
            width: '50%',
        },
        formIterator: {
            '& section': {
            },
        },
    }),
    {
        name: 'ArrayInputView',
    },
);

const ArrayInputView = ({
    children,
    source,
    itemResource,
    ...rest
}): JSX.Element => {
    const classes = useArrayInputViewStyles();
    const translate = useTranslate();

    return (

        <ArrayInput
            {...rest}
            source={source}
            label={itemResource ? translate(`resources.${itemResource}.name`, 2) : ''}
        >
            <Card
                sx={{
                    width: '100%',
                    marginBottom: '20px',
                }}
            >
                <CardContent>
                    <SimpleFormIterator
                        source={source}
                        className={classes.formIterator}
                        disableReordering
                        getItemLabel={(index) => `${translate(`resources.${itemResource}.name`, 1)} ${index + 1}.`}
                        addButton={(
                            <AssignItemButton
                                label='label.entity.button.assign'
                                icon={<AddLinkIcon />}
                            />
                        )}
                    >
                        {children}
                    </SimpleFormIterator>
                </CardContent>
            </Card>
        </ArrayInput>

    );
};

export default ArrayInputView;
