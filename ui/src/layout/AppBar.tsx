// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import * as React from 'react';
import {
    AppBar,
    UserMenu,
} from 'react-admin';
import {
    Box,
    Typography,
    useMediaQuery,
    Theme,
} from '@mui/material';

// import Logo from './Logo';

const CustomAppBar = (props: any): JSX.Element => {
    const isLargeEnough = useMediaQuery<Theme>((theme) => theme.breakpoints.up('sm'));

    return (
        <AppBar
            {...props}
            color='secondary'
            elevation={1}
            userMenu={<UserMenu />}
        >
            <Typography
                variant='h6'
                color='inherit'
                sx={{
                    flex: 1,
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap',
                    overflow: 'hidden',
                }}
                id='react-admin-title'
            />
            {/* isLargeEnough && <Logo /> */}
            {isLargeEnough && <Box component='span' sx={{ flex: 1 }} />}
        </AppBar>
    );
};

export default CustomAppBar;
