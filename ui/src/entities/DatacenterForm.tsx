// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    useTranslate,
} from 'react-admin';

import icons from '../icons';
import {
    Accordion,
} from '../common';
import EntityForm from './EntityForm';
import EntityAssignmentForm from './EntityAssignmentForm';

const DatacenterForm = (props): JSX.Element => {
    const translate = useTranslate();

    return (
        <EntityForm {...props}>
            <Accordion icon={icons.hosts} headingText={translate('label.entity.form.hosts')} defaultExpanded>
                <EntityAssignmentForm reference='hosts' source='hosts' itemResource='hosts' />
            </Accordion>
        </EntityForm>
    );
};

export default DatacenterForm;
