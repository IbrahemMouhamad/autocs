// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import SettingsSuggestIcon from '@mui/icons-material/SettingsSuggest';
import {
    useResourceContext,
    useGetOne,
    useTranslate,
} from 'react-admin';

import { Accordion } from '../common';
import { ConfigurationInputs } from '../configurations';

const AdvancedConfigurationForm = (): JSX.Element | null => {
    const resource = useResourceContext();
    const { data, isLoading } = useGetOne('configurations', { id: resource.slice(0, -1) });
    const translate = useTranslate();

    return !isLoading && Object.keys(data.configuration).length > 0 ? (
        <Accordion icon={SettingsSuggestIcon} headingText={translate('label.entity.form.advanced')} defaultExpanded>
            <ConfigurationInputs config={data.configuration} name='details' title='' />
        </Accordion>
    ) : null;
};

export default AdvancedConfigurationForm;
