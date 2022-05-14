// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    Form,
    Toolbar,
    SaveButton,
} from 'react-admin';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';

import BasicConfigurationForm from './BasicConfigurationForm';
import AdvancedConfigurationForm from './AdvancedConfigurationForm';

const EntityForm = ({ edit, children }): JSX.Element => (
    <Form>
        <BasicConfigurationForm />
        <AdvancedConfigurationForm />
        {children}
        <Toolbar>
            <SaveButton
                label={edit ? 'ra.action.edit' : 'ra.action.create'}
                icon={edit ? <EditIcon /> : <AddIcon />}
            />
        </Toolbar>
    </Form>
);

export default EntityForm;
