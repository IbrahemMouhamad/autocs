// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { FormDataConsumer } from 'react-admin';

import { ArrayInputView } from '../common';

import EntityConfigurationForm from './EntityConfigurationForm';

const EntityAssignmentForm = (props): JSX.Element => {
    const { reference } = props;

    return (
        <ArrayInputView
            {...props}
        >
            <FormDataConsumer>
                {({ getSource }) => getSource && (
                    <EntityConfigurationForm getSource={getSource} reference={reference} />
                )}
            </FormDataConsumer>
        </ArrayInputView>
    );
};

export default EntityAssignmentForm;
