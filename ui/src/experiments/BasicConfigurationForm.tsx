// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import FoundationIcon from '@mui/icons-material/Foundation';
import {
    required,
    useTranslate,
    minValue,
    maxValue,
} from 'react-admin';

import {
    Accordion,
    useAccordionStyles,
    TextInput,
    NumberInput,
} from '../common';

const validateRunsField = [required(), minValue(1), maxValue(100)];

const BasicConfigurationForm = (): JSX.Element => {
    const translate = useTranslate();
    const classes = useAccordionStyles();

    return (
        <Accordion icon={FoundationIcon} headingText={translate('label.experiment.form.basic')} expanded>
            <div className={classes.column}>
                <TextInput
                    source='name'
                    validate={required()}
                    helpText='resources.experiments.helpText.name'
                    isTranslatable
                />
            </div>
            <div className={classes.column}>
                <TextInput
                    source='description'
                    validate={required()}
                    helpText='resources.experiments.helpText.description'
                    isTranslatable
                />
            </div>
            <div className={classes.column}>
                <NumberInput
                    source='runs'
                    validate={validateRunsField}
                    helpText='resources.experiments.helpText.runs'
                    isTranslatable
                />
            </div>
        </Accordion>
    );
};

export default BasicConfigurationForm;
