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

const HostsConfigurationForm = (props): JSX.Element => (
    <ArrayInputView {...props}>
        <FormDataConsumer>
            {({ getSource }) => getSource && (
                <EntityConfigurationForm getSource={getSource} reference='hosts' />
            )}
        </FormDataConsumer>
    </ArrayInputView>
);

const DatacenterConfigurationForm = (props): JSX.Element | null => (
    <ArrayInputView source='datacenters' itemResource='datacenters' {...props}>
        <FormDataConsumer>
            {({ getSource }) => getSource && (
                <>
                    <EntityConfigurationForm getSource={getSource} reference='datacenters' />
                    <HostsConfigurationForm source={getSource('hosts')} itemResource='hosts' {...props} />
                </>
            )}
        </FormDataConsumer>
    </ArrayInputView>
);

const DatacentersConfigurationForm = (props): JSX.Element => {
    const translate = useTranslate();

    return (
        <Accordion icon={icons.datacenters} headingText={translate('label.experiment.form.datacenters')} defaultExpanded>
            <DatacenterConfigurationForm {...props} />
        </Accordion>
    );
};

export default DatacentersConfigurationForm;
