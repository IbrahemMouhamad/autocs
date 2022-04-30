// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    useTranslate,
    FormDataConsumer,
} from 'react-admin';

import icons from '../icons';
import {
    Accordion,
    ArrayInputView,
} from '../common';

import EntityConfigurationForm from './EntityConfigurationForm';

const CloudletsConfigurationForm = (props): JSX.Element => (
    <ArrayInputView {...props}>
        <FormDataConsumer>
            {({ getSource }) => getSource && (
                <EntityConfigurationForm getSource={getSource} reference='cloudlets' />
            )}
        </FormDataConsumer>
    </ArrayInputView>
);

const VMsConfigurationForm = (props): JSX.Element => (
    <ArrayInputView {...props}>
        <FormDataConsumer>
            {({ getSource }) => getSource && (
                <EntityConfigurationForm getSource={getSource} reference='vms' />
            )}
        </FormDataConsumer>
    </ArrayInputView>
);

const BrokerConfigurationForm = (props): JSX.Element | null => (
    <ArrayInputView source='brokers' itemResource='brokers' {...props}>
        <FormDataConsumer>
            {({ getSource }) => getSource && (
                <>
                    <EntityConfigurationForm getSource={getSource} reference='brokers' />
                    <VMsConfigurationForm source={getSource('vms')} itemResource='vms' {...props} />
                    <CloudletsConfigurationForm source={getSource('cloudlets')} itemResource='cloudlets' {...props} />
                </>
            )}
        </FormDataConsumer>
    </ArrayInputView>
);

const BrokersConfigurationForm = (props): JSX.Element => {
    const translate = useTranslate();

    return (
        <Accordion icon={icons.brokers} headingText={translate('label.experiment.form.brokers')} defaultExpanded>
            <BrokerConfigurationForm {...props} />
        </Accordion>
    );
};

export default BrokersConfigurationForm;
