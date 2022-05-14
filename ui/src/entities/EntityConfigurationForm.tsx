// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import Grid from '@mui/material/Grid';
import {
    ReferenceInput,
    required,
    minValue,
    maxValue,
    TextInput,
} from 'react-admin';

import {
    SelectInput,
    NumberInput,
} from '../common';

const validateAmountField = [required(), minValue(1), maxValue(100)];

export const EntityInput = ({ source, reference, ...rest }): JSX.Element => (
    <ReferenceInput
        source={source}
        reference={reference}
    >
        <SelectInput
            validate={required()}
            label='resources.scenarios.fields.entities.id'
            helpText='resources.scenarios.helpText.entities.id'
            isTranslatable
            {...rest}
        />
    </ReferenceInput>
);

const AmountInput = (props): JSX.Element => (
    <NumberInput
        validate={validateAmountField}
        label='resources.scenarios.fields.entities.amount'
        helpText='resources.scenarios.helpText.entities.amount'
        isTranslatable
        {...props}
    />
);

const EntityConfigurationForm = ({ getSource, reference }): JSX.Element => (
    <Grid container spacing={3} rowSpacing={1}>
        <Grid item sm={6}>
            <EntityInput source={getSource('id')} reference={reference} />
        </Grid>
        <Grid item sm={6}>
            <AmountInput source={getSource('amount')} />
        </Grid>
        <TextInput
            source={getSource('type')}
            defaultValue={reference}
            style={{ display: 'none' }}
        />
    </Grid>
);

export default EntityConfigurationForm;
