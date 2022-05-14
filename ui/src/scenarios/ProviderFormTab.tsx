// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    CreateButton,
    useGetList,
    useRecordContext,
    useTranslate,
    TopToolbar,
} from 'react-admin';

import { FormTab } from '../common';
import { EntityAssignmentForm, EntityInput } from '../entities';
import icons from '../icons';

const ProviderFormTab = (props): JSX.Element | null => {
    const record = useRecordContext();
    if (!record || record.id == null) {
        return null;
    }
    const { isLoading, total } = useGetList('providers');
    const translate = useTranslate();
    let label = translate('label.scenario.tabs.providers');
    if (!isLoading) {
        label += ` (${total})`;
    }
    return (
        <FormTab label={label} {...props} icon={React.createElement(icons.providers)}>
            <TopToolbar
                sx={{
                    width: '100%',
                }}
            >
                <CreateButton
                    resource='providers'
                    label='label.entity.button.add_provider'
                />
            </TopToolbar>
            <EntityInput source='provider.id' reference='providers' />
            <EntityAssignmentForm reference='brokers' source='brokers' itemResource='brokers' />
        </FormTab>
    );
};

export default ProviderFormTab;
