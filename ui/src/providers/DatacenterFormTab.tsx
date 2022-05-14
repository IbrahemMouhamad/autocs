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
import { EntityAssignmentForm } from '../entities';
import icons from '../icons';

const DatacenterFormTab = (props): JSX.Element | null => {
    const record = useRecordContext();
    if (!record || record.id == null) {
        return null;
    }
    const { isLoading, total } = useGetList('datacenters');
    const translate = useTranslate();
    let label = translate('label.provider.tabs.datacenters');
    if (!isLoading) {
        label += ` (${total})`;
    }
    return (
        <FormTab label={label} {...props} icon={React.createElement(icons.datacenters)}>
            <TopToolbar
                sx={{
                    width: '100%',
                }}
            >
                <CreateButton
                    resource='datacenters'
                    label='label.entity.button.add_datacenter'
                />
            </TopToolbar>
            <EntityAssignmentForm reference='datacenters' source='datacenters' itemResource='datacenters' />
        </FormTab>
    );
};

export default DatacenterFormTab;
