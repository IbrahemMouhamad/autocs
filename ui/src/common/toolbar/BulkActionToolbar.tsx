// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { BulkDeleteButton } from 'react-admin';

const BulkActionToolbar = ({ children = undefined }): JSX.Element => (
    <>
        <BulkDeleteButton mutationMode='optimistic' />
        {children}
    </>
);

export default BulkActionToolbar;
