import {Button, ButtonGroup, Card, IconButton, Tab, TabList, TabPanel, Tabs} from "@mui/joy";


type Props = {
    server: number | undefined
    className?: string
}


export const Server = ({server, className}: Props) => {
    if (server !== undefined) {
        return (
            <Card className={className}>
                <Tabs size="sm" className={'flex h-full w-full overflow-y-auto'}>
                    <div className={'h-16 flex w-full'}>
                        <p className={'text-lg font-bold ml-8'}>ARK: Survival Ascended server {server}</p>
                        <div className={'ml-auto my-auto mr-8'}>
                            <ButtonGroup aria-label="outlined primary button group">
                                <Button color={'success'} variant="solid">Start</Button>
                                <Button color={'danger'} variant="solid">Stop</Button>
                            </ButtonGroup>
                        </div>
                    </div>
                    <TabList className={'w-full'}>
                        <Tab variant="plain" indicatorInset color="neutral">General settings</Tab>
                        <Tab variant="plain" indicatorInset color="neutral">Mods</Tab>
                        <Tab variant="plain" indicatorInset color="neutral">Plugins</Tab>
                        <Tab variant="plain" indicatorInset color="neutral">Modifiers</Tab>
                    </TabList>
                    <TabPanel className={'min-h-screen'}></TabPanel>
                </Tabs>
            </Card>
        );
    } else {
        return (
            <Card className={className}>
                <Tabs size="sm" className={'flex h-full w-full overflow-y-auto'}>
                    <div className={'h-16 flex w-full'}>
                        <p className={'text-lg font-bold ml-8'}>No server found/selected</p>
                    </div>
                </Tabs>
            </Card>
        );
    }
};