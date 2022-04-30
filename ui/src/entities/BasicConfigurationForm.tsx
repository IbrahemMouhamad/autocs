// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import FoundationIcon from '@mui/icons-material/Foundation';
import {
    required,
    useTranslate,
    useResourceContext,
} from 'react-admin';

import { Accordion, useAccordionStyles, TextInput } from '../common';

const BasicConfigurationForm = (): JSX.Element => {
    const resource = useResourceContext();
    const translate = useTranslate();
    const classes = useAccordionStyles();

    return (
        <Accordion icon={FoundationIcon} headingText={translate('label.entity.form.basic')} expanded>
            <div className={classes.column}>
                <TextInput
                    source='name'
                    validate={required()}
                    helpText={`resources.${resource}.helpText.name`}
                    isTranslatable
                />
            </div>
            <div style={{ width: '100%' }}>
                <TextInput
                    source='description'
                    validate={required()}
                    helpText={`resources.${resource}.helpText.description`}
                    isTranslatable
                />
            </div>
        </Accordion>
    );
};

export default BasicConfigurationForm;
